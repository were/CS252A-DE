// See LICENSE.txt for license details.
package adder

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class FullAdder4Tests(adder: FullAdder4) extends PeekPokeTester(adder) {
  for (i <- 0 until 100) {
    val a = rnd.nextInt(1 << 4)
    val b = rnd.nextInt(1 << 4)
    poke(adder.io.a, a)
    poke(adder.io.b, b)
    step(1)
    expect(adder.io.sum, (a + b) & ((1 << 4) - 1))
    expect(adder.io.cout, ((a + b) >> 4) & 1)
  }
}

class FullAdder4Tester extends ChiselFlatSpec {
  behavior of "Adder"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers in $backend" in {
      Driver(() => new FullAdder4(), backend)(c => new FullAdder4Tests(c)) should be (true)
    }
  }
}
