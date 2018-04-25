package adder

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class SelectAdder6Tests(adder: SelectAdder6) extends PeekPokeTester(adder) {
  for (i <- 0 until 100) {
    val a = rnd.nextInt(1 << 6)
    val b = rnd.nextInt(1 << 6)
    poke(adder.io.a, a)
    poke(adder.io.b, b)
    step(1)
    expect(adder.io.sum0, (a + b) & ((1 << 6) - 1))
    expect(adder.io.sum1, (a + b + 1) & ((1 << 6) - 1))
    expect(adder.io.cout0, ((a + b) >> 6) & 1)
    expect(adder.io.cout1, ((a + b + 1) >> 6) & 1)
  }
}

class SelectAdder6Tester extends ChiselFlatSpec {
  behavior of "Adder"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers in $backend" in {
      Driver(() => new SelectAdder6(), backend)(c => new SelectAdder6Tests(c)) should be (true)
    }
  }
}
