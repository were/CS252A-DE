package adder

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class SelectAdder8Tests(adder: SelectAdder8) extends PeekPokeTester(adder) {
  for (i <- 0 until 100) {
    val a = rnd.nextInt(1 << 8)
    val b = rnd.nextInt(1 << 8)
    poke(adder.io.a, a)
    poke(adder.io.b, b)
    step(1)
    expect(adder.io.sum0, (a + b) & ((1 << 8) - 1))
    expect(adder.io.sum1, (a + b + 1) & ((1 << 8) - 1))
    expect(adder.io.cout0, ((a + b) >> 8) & 1)
    expect(adder.io.cout1, ((a + b + 1) >> 8) & 1)
  }
}

class SelectAdder8Tester extends ChiselFlatSpec {
  behavior of "Adder"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers in $backend" in {
      Driver(() => new SelectAdder8(), backend)(c => new SelectAdder8Tests(c)) should be (true)
    }
  }
}
