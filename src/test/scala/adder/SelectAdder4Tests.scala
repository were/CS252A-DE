package adder

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class SelectAdder4Tests(adder: SelectAdder4) extends PeekPokeTester(adder) {
  for (i <- 0 until 100) {
    val a = rnd.nextInt(1 << 4)
    val b = rnd.nextInt(1 << 4)
    poke(adder.io.a, a)
    poke(adder.io.b, b)
    step(1)
    expect(adder.io.sum0, (a + b) & ((1 << 4) - 1))
    expect(adder.io.sum1, (a + b + 1) & ((1 << 4) - 1))
    expect(adder.io.cout0, ((a + b) >> 4) & 1)
    expect(adder.io.cout1, ((a + b + 1) >> 4) & 1)
  }
}

class SelectAdder4Tester extends ChiselFlatSpec {
  behavior of "Adder"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers in $backend" in {
      Driver(() => new SelectAdder4(), backend)(c => new SelectAdder4Tests(c)) should be (true)
    }
  }
}
