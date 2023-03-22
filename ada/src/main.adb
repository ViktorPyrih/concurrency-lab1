with Ada.Text_Io, Ada.Integer_Text_IO;
use Ada.Text_IO, Ada.Integer_Text_IO;

procedure Main is

   IsInterrupted : boolean := false;
   pragma Volatile(IsInterrupted);
   ThreadsCount : Integer := 10;
   Step: Integer := 10;
   Magic : Integer := 0;

   -- break thread
   task type break_thread;
   task body break_thread is
   begin
      delay 10.0;
      IsInterrupted := true;
   end break_thread;

   -- worker thread
   task type worker_thread is
      entry Start;
      entry Finish(Sum : out Integer; Elements : out Integer);
   end worker_thread;

   task body worker_thread is
      Sum : Integer := 0;
      Elements : Integer := 0;
   begin
      accept Start;

      loop
         Sum := Sum + Step;
         Elements := Elements + 1;
         Magic := Magic + 1;
         exit when IsInterrupted;
      end loop;
      accept Finish (Sum : out Integer; Elements : out Integer) do
         Sum := worker_thread.Sum;
         Elements := worker_thread.Elements;
      end Finish;
   end worker_thread;

   Workers : Array(1..ThreadsCount) of worker_thread;
   Sums : Array(1..ThreadsCount) of Integer;
   Elements : Array(1..ThreadsCount) of Integer;

   Breaker : break_thread;

-- body
begin

   for i in Workers'Range loop
      Workers(i).Start;
   end loop;

   for i in Workers'Range loop
      Workers(i).Finish(Sums(i), Elements(i));
   end loop;

   for i in Sums'Range loop
      Put("Sum from Thread");
      Put(i'Img);
      Put(" =");
      Put(Sums(i)'Img);
      Put(", elements' count =");
      Put(Elements(i)'Img);
      Put_Line("");
   end loop;

end Main;
