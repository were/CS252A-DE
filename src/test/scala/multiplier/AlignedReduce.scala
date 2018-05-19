// See LICENSE.txt for license details.
package multiplier

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class AlignedReduceTests(mul: AlignedReduce) extends PeekPokeTester(mul) {
  for (i <- 0 until 10) {
    val a = rnd.nextInt(1 << 18)
    val b = rnd.nextInt(1 << 24)
    poke(mul.io.a, a)
    poke(mul.io.b, b)
    step(1)
    //printf("%d %d\n", a, b)
    expect(mul.io.out, ((a << 3) + b) & ((1 << 24) - 1))
  }
}

class AlignedReduceTester extends ChiselFlatSpec {
  behavior of "MultiplierTester"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers in $backend" in {
      Driver(() => new AlignedReduce(18, 24, List(4, 4, 4, 3, 3)), backend)(c => new AlignedReduceTests(c)) should be (true)
    }
  }
}
