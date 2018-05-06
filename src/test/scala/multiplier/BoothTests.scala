// See LICENSE.txt for license details.
package multiplier

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class BoothTests(booth: Booth) extends PeekPokeTester(booth) {
  for (i <- 0 until 100) {
    val a = rnd.nextInt(1 << 3)
    val b = rnd.nextInt(1 << 8)
    poke(booth.io.a, a)
    poke(booth.io.b, b)
    step(1)
    printf("%d %d\n", a, b)
    if (a == 0 || a == 7) {
      expect(booth.io.out, 0)
      expect(booth.io.cout, 0)
    } else if (a == 1 || a == 2) {
      expect(booth.io.out, b)
      expect(booth.io.cout, 0)
    } else if (a == 5 || a == 6) {
      expect(booth.io.out, ~b & 1023)
      expect(booth.io.cout, 1)
    } else if (a == 3) {
      expect(booth.io.out, b << 1 & 1023)
      expect(booth.io.cout, 0)
    } else if (a == 4) {
      expect(booth.io.out, ~(b << 1) & 1023)
      expect(booth.io.cout, 1)
    }
  }
}

class BoothTester extends ChiselFlatSpec {
  behavior of "BoothTester"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers in $backend" in {
      Driver(() => new Booth(8), backend)(c => new BoothTests(c)) should be (true)
    }
  }
}
