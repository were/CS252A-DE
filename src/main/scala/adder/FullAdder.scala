package adder

import chisel3._

class FullAdder6 extends Module {

  val io = IO(new Bundle { 
    val a    = Input(UInt(6.W))
    val b    = Input(UInt(6.W))
    val sum  = Output(UInt(6.W))
    val cout = Output(UInt(1.W))
  })
  
  val m0 = Module(new OrXor())
  m0.io.a := io.a(0)
  m0.io.b := io.b(0)

  val m1 = Module(new FullAdder())
  m1.io.a := io.a(1)
  m1.io.b := io.b(1)
  m1.io.cin := m0.io.cout

  val m2 = Module(new FullAdder())
  m2.io.a := io.a(2)
  m2.io.b := io.b(2)
  m2.io.cin := m1.io.cout

  val m3 = Module(new FullAdder())
  m3.io.a := io.a(3)
  m3.io.b := io.b(3)
  m3.io.cin := m2.io.cout

  val m4 = Module(new FullAdder())
  m4.io.a := io.a(4)
  m4.io.b := io.b(4)
  m4.io.cin := m3.io.cout

  val m5 = Module(new FullAdder())
  m5.io.a := io.a(5)
  m5.io.b := io.b(5)
  m5.io.cin := m4.io.cout

  io.sum :=  m0.io.sum |  (m1.io.sum << 1) |  (m2.io.sum << 2) |  (m3.io.sum << 3) | (m4.io.sum << 4) | (m5.io.sum << 5)
  io.cout := m5.io.cout

}

class FullAdder4 extends Module {

  val io = IO(new Bundle { 
    val a    = Input(UInt(4.W))
    val b    = Input(UInt(4.W))
    val sum  = Output(UInt(4.W))
    val cout = Output(UInt(1.W))
  })
  
  val m0 = Module(new OrXor())
  m0.io.a := io.a(0)
  m0.io.b := io.b(0)

  val m1 = Module(new FullAdder())
  m1.io.a := io.a(1)
  m1.io.b := io.b(1)
  m1.io.cin := m0.io.cout

  val m2 = Module(new FullAdder())
  m2.io.a := io.a(2)
  m2.io.b := io.b(2)
  m2.io.cin := m1.io.cout

  val m3 = Module(new FullAdder())
  m3.io.a := io.a(3)
  m3.io.b := io.b(3)
  m3.io.cin := m2.io.cout

  io.sum :=  m0.io.sum |  (m1.io.sum << 1) |  (m2.io.sum << 2) |  (m3.io.sum << 3)
  io.cout := m3.io.cout

}

