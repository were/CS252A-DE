// See LICENSE.txt for license details.
package adder

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class FullAdder6Tests(adder: FullAdder6) extends PeekPokeTester(adder) {
  for (i <- 0 until 100) {
    val a = rnd.nextInt(1 << 6)
    val b = rnd.nextInt(1 << 6)
    poke(adder.io.a, a)
    poke(adder.io.b, b)
    step(1)
    expect(adder.io.sum, (a + b) & ((1 << 6) - 1))
    expect(adder.io.cout, ((a + b) >> 6) & 1)
  }
}

class FullAdder6Tester extends ChiselFlatSpec {
  behavior of "Adder"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers in $backend" in {
      Driver(() => new FullAdder6(), backend)(c => new FullAdder6Tests(c)) should be (true)
    }
  }
}
