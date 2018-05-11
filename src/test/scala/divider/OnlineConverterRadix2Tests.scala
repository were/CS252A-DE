// See LICENSE.txt for license details.
package examples

import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

class OnlineConverterRadix2Tests(c: OnlineConverterRadix2) extends PeekPokeTester(c) {
  for (t <- 0 until 1) {
    poke(c.io.load, 1)
    step(1)
    // Test the initialization step.
    poke(c.io.load, 0)
    // Compute.
    // q = 1, -1, 1, 1, -1
    val qms = Array(1, 1, 1, 1, 1)
    val qss = Array(0, 1, 0, 0, 1)
    val qs = Array(1, -1, 1, 1, -1)
    var quotient = 0
    for (i <- 0 until 5) {
      poke(c.io.qm, qms(i))
      poke(c.io.qs, qss(i))
      step(1)
      quotient = (quotient << 1) + qs(i)
      println("Quotient " + quotient)
      expect(c.io.quotient, quotient)
    }
  }
}

class OnlineConverterRadix2Tester extends ChiselFlatSpec {
  behavior of "OnlineConverterRadix2"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers $backend" in {
      Driver(() => new OnlineConverterRadix2(8), backend)(c => new OnlineConverterRadix2Tests(c)) should be (true)
    }
  }
}
