package adder

import chisel3._

class SelectAdder4 extends Module {
  val io = IO(new Bundle { 
    val a    = Input(UInt(4.W))
    val b    = Input(UInt(4.W))
    val sum0  = Output(UInt(4.W))
    val sum1  = Output(UInt(4.W))
    val cout0 = Output(UInt(1.W))
    val cout1 = Output(UInt(1.W))
  })
  
  val m0 = Module(new OrXor())
  m0.io.a := io.a(0)
  m0.io.b := io.b(0)

  val _m0 = Module(new OrXor())
  _m0.io.a := m0.io.sum
  _m0.io.b := 1.U

  val m1 = Module(new FullAdder())
  m1.io.a := io.a(1)
  m1.io.b := io.b(1)
  m1.io.cin := m0.io.cout

  val _m1 = Module(new OrXor())
  _m1.io.a := m1.io.sum
  _m1.io.b := _m0.io.cout

  val m2 = Module(new FullAdder())
  m2.io.a := io.a(2)
  m2.io.b := io.b(2)
  m2.io.cin := m1.io.cout

  val _m2 = Module(new OrXor())
  _m2.io.a := m2.io.sum
  _m2.io.b := _m1.io.cout

  val m3 = Module(new FullAdder())
  m3.io.a := io.a(3)
  m3.io.b := io.b(3)
  m3.io.cin := m2.io.cout

  val _m3 = Module(new OrXor())
  _m3.io.a := m3.io.sum
  _m3.io.b := _m2.io.cout

  io.sum0 :=  m0.io.sum |  (m1.io.sum << 1) |  (m2.io.sum << 2) |  (m3.io.sum << 3)
  io.sum1 := _m0.io.sum | (_m1.io.sum << 1) | (_m2.io.sum << 2) | (_m3.io.sum << 3)

  io.cout0 := m3.io.cout
  io.cout1 := _m3.io.cout | m3.io.cout

}

class SelectAdder6 extends Module {
  val io = IO(new Bundle { 
    val a    = Input(UInt(6.W))
    val b    = Input(UInt(6.W))
    val sum0  = Output(UInt(6.W))
    val sum1  = Output(UInt(6.W))
    val cout0 = Output(UInt(1.W))
    val cout1 = Output(UInt(1.W))
  })
  
  val m0 = Module(new OrXor())
  m0.io.a := io.a(0)
  m0.io.b := io.b(0)

  val _m0 = Module(new OrXor())
  _m0.io.a := m0.io.sum
  _m0.io.b := 1.U

  val m1 = Module(new FullAdder())
  m1.io.a := io.a(1)
  m1.io.b := io.b(1)
  m1.io.cin := m0.io.cout

  val _m1 = Module(new OrXor())
  _m1.io.a := m1.io.sum
  _m1.io.b := _m0.io.cout

  val m2 = Module(new FullAdder())
  m2.io.a := io.a(2)
  m2.io.b := io.b(2)
  m2.io.cin := m1.io.cout

  val _m2 = Module(new OrXor())
  _m2.io.a := m2.io.sum
  _m2.io.b := _m1.io.cout

  val m3 = Module(new FullAdder())
  m3.io.a := io.a(3)
  m3.io.b := io.b(3)
  m3.io.cin := m2.io.cout

  val _m3 = Module(new OrXor())
  _m3.io.a := m3.io.sum
  _m3.io.b := _m2.io.cout

  val m4 = Module(new FullAdder())
  m4.io.a := io.a(4)
  m4.io.b := io.b(4)
  m4.io.cin := m3.io.cout

  val _m4 = Module(new OrXor())
  _m4.io.a := m4.io.sum
  _m4.io.b := _m3.io.cout

  val m5 = Module(new FullAdder())
  m5.io.a := io.a(5)
  m5.io.b := io.b(5)
  m5.io.cin := m4.io.cout

  val _m5 = Module(new OrXor())
  _m5.io.a := m5.io.sum
  _m5.io.b := _m4.io.cout

  io.sum0 :=  m0.io.sum |  (m1.io.sum << 1) |  (m2.io.sum << 2) |  (m3.io.sum << 3) |  (m4.io.sum << 4) |  (m5.io.sum << 5)
  io.sum1 := _m0.io.sum | (_m1.io.sum << 1) | (_m2.io.sum << 2) | (_m3.io.sum << 3) | (_m4.io.sum << 4) | (_m5.io.sum << 5)

  io.cout0 := m5.io.cout
  io.cout1 := _m5.io.cout | m5.io.cout

}

class SelectAdder8 extends Module {
  val io = IO(new Bundle { 
    val a    = Input(UInt(8.W))
    val b    = Input(UInt(8.W))
    val sum0  = Output(UInt(8.W))
    val sum1  = Output(UInt(8.W))
    val cout0 = Output(UInt(1.W))
    val cout1 = Output(UInt(1.W))
  })
  
  val m0 = Module(new OrXor())
  m0.io.a := io.a(0)
  m0.io.b := io.b(0)

  val _m0 = Module(new OrXor())
  _m0.io.a := m0.io.sum
  _m0.io.b := 1.U

  val m1 = Module(new FullAdder())
  m1.io.a := io.a(1)
  m1.io.b := io.b(1)
  m1.io.cin := m0.io.cout

  val _m1 = Module(new OrXor())
  _m1.io.a := m1.io.sum
  _m1.io.b := _m0.io.cout

  val m2 = Module(new FullAdder())
  m2.io.a := io.a(2)
  m2.io.b := io.b(2)
  m2.io.cin := m1.io.cout

  val _m2 = Module(new OrXor())
  _m2.io.a := m2.io.sum
  _m2.io.b := _m1.io.cout

  val m3 = Module(new FullAdder())
  m3.io.a := io.a(3)
  m3.io.b := io.b(3)
  m3.io.cin := m2.io.cout

  val _m3 = Module(new OrXor())
  _m3.io.a := m3.io.sum
  _m3.io.b := _m2.io.cout

  val m4 = Module(new FullAdder())
  m4.io.a := io.a(4)
  m4.io.b := io.b(4)
  m4.io.cin := m3.io.cout

  val _m4 = Module(new OrXor())
  _m4.io.a := m4.io.sum
  _m4.io.b := _m3.io.cout

  val m5 = Module(new FullAdder())
  m5.io.a := io.a(5)
  m5.io.b := io.b(5)
  m5.io.cin := m4.io.cout

  val _m5 = Module(new OrXor())
  _m5.io.a := m5.io.sum
  _m5.io.b := _m4.io.cout

  val m6 = Module(new FullAdder())
  m6.io.a := io.a(6)
  m6.io.b := io.b(6)
  m6.io.cin := m5.io.cout

  val _m6 = Module(new OrXor())
  _m6.io.a := m6.io.sum
  _m6.io.b := _m5.io.cout

  val m7 = Module(new FullAdder())
  m7.io.a := io.a(7)
  m7.io.b := io.b(7)
  m7.io.cin := m6.io.cout

  val _m7 = Module(new OrXor())
  _m7.io.a := m7.io.sum
  _m7.io.b := _m6.io.cout

  io.sum0 :=  m0.io.sum |  (m1.io.sum << 1) |  (m2.io.sum << 2) |  (m3.io.sum << 3) |  (m4.io.sum << 4) |  (m5.io.sum << 5) |  (m6.io.sum << 6) |  (m7.io.sum << 7)
  io.sum1 := _m0.io.sum | (_m1.io.sum << 1) | (_m2.io.sum << 2) | (_m3.io.sum << 3) | (_m4.io.sum << 4) | (_m5.io.sum << 5) | (_m6.io.sum << 6) | (_m7.io.sum << 7)

  io.cout0 := m7.io.cout
  io.cout1 := _m7.io.cout | m7.io.cout

}