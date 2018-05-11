// See LICENSE.txt for license details.
package examples

import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

class DividerRadix2UpdaterTests(c: DividerRadix2Updater) extends PeekPokeTester(c) {
  for (t <- 0 until 1) {
    val rnd0 = 0x9F
    val rnd1 = 0xC5
    poke(c.io.ws, rnd0 >> 1)
    poke(c.io.wc, 0)
    poke(c.io.divisor, rnd1)
    // Test the initialization step.
    println("Send in " + rnd0.toBinaryString + " / " + rnd1.toBinaryString)
    for (iter <- 0 until 1) {
      step(1)
      println("Step " + iter)
      expect(c.io.qs, 0)
      expect(c.io.qm, 1)
      expect(c.io.ows, 0x3A5)
      expect(c.io.owc, 0x34)
      expect(c.io.debug_q, 1)
      expect(c.io.debug_csa_a, 0x9E)
      expect(c.io.debug_csa_b, 0x1)
      expect(c.io.debug_csa_cin, 0x33A)
    }
  }
}

class DividerRadix2UpdaterTester extends ChiselFlatSpec {
  behavior of "DividerRadix2Updater"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers $backend" in {
      Driver(() => new DividerRadix2Updater(8), backend)(c => new DividerRadix2UpdaterTests(c)) should be (true)
    }
  }
}

