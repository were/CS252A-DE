// See LICENSE.txt for license details.
package multiplier

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class BoothTests(booth: Booth, width: Int) extends PeekPokeTester(booth) {
  for (i <- 0 until 100) {
    val a = rnd.nextInt(1 << 3)
    val b = rnd.nextInt(1 << width)
    poke(booth.io.a, a)
    poke(booth.io.b, b)
    step(1)
    //printf("%d %d\n", a, b)

    val lut = Array(0, 1, 1, 2, -2, -1, -1, 0);
    val out = lut(a) * b
    val all = (1 << (width + 2)) - 1
    val hgh = (1 << (width + 1))
    if (lut(a) < 0) {
      expect(booth.io.cout, 1)
      //printf("%d %d %d\n", out, out & all, out & all ^ hgh)
      expect(booth.io.out, (out & all ^ hgh) - 1)
    } else {
      expect(booth.io.cout, 0)
      //printf("%d %d %d\n", out, out & all, out & all ^ hgh)
      expect(booth.io.out, out & all ^ hgh)
    }
  }
}

class BoothTester extends ChiselFlatSpec {
  behavior of "BoothTester"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers in $backend" in {
      Driver(() => new Booth(8), backend)(c => new BoothTests(c, 8)) should be (true)
    }
  }
}
