// See LICENSE.txt for license details.
package multiplier

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class Multiplier8Tests(mul: Multiplier) extends PeekPokeTester(mul) {
  for (i <- 0 until 1) {
    val a = rnd.nextInt(1 << 8)
    val b = rnd.nextInt(1 << 8)
    poke(mul.io.a, a)
    poke(mul.io.b, b)
    step(1)
    printf("%d %d\n", a, b)
    expect(mul.io.c, a * b)
  }
}

class MultiplierTester extends ChiselFlatSpec {
  behavior of "MultiplierTester"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers in $backend" in {
      Driver(() => new Multiplier(8), backend)(c => new Multiplier8Tests(c)) should be (true)
    }
  }
}
