// See LICENSE.txt for license details.
package examples

import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

class DividerRadix2Tests(c: DividerRadix2) extends PeekPokeTester(c) {
  for (t <- 0 until 1) {
    val rnd0 = 0x9E
    val rnd1 = 0xC5
    poke(c.io.dividend, rnd0)
    poke(c.io.divisor, rnd1)
    poke(c.io.load, 1)
    step(1)
    // Test the initialization step.
    println("Send in " + rnd0.toBinaryString + " / " + rnd1.toBinaryString)
    poke(c.io.load, 0)
    // Compute.
    val quotients = Array(1, 1, 3, 7, 13)
    for (i <- 0 until 5) {
      step(1)
      expect(c.io.quotient, quotients(i))
    }
  }
}

class DividerRadix2Tester extends ChiselFlatSpec {
  behavior of "DividerRadix2"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers $backend" in {
      Driver(() => new DividerRadix2(8, 1), backend)(c => new DividerRadix2Tests(c)) should be (true)
    }
  }
}

