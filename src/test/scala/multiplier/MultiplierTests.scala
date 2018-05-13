// See LICENSE.txt for license details.
package multiplier

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class MultiplierTests(mul: Multiplier16x16) extends PeekPokeTester(mul) {
  for (i <- 0 until 10) {
    val a = rnd.nextInt(1 << 15)
    val b = rnd.nextInt(1 << 15)
    //val a = 8528
    //val b = 12034
    poke(mul.io.a, a)
    poke(mul.io.b, b)
    step(1)
    printf("%d %d\n", a, b)
    expect(mul.io.c, a * b)
  }
}

class Multiplier16Tester extends ChiselFlatSpec {
  behavior of "MultiplierTester"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers in $backend" in {
      Driver(() => new Multiplier16x16(), backend)(c => new MultiplierTests(c)) should be (true)
    }
  }
}
