// See LICENSE.txt for license details.
package examples

import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

class Radix4OnlineConverterControlTests(c: Radix4OnlineConverterControl) extends PeekPokeTester(c) {
  for (qs <- 0 until 2) {
    for (qm <- 0 until 3) {
      if (qs != 1 || qm != 0) {
        poke(c.io.qm, qm)
        poke(c.io.qs, qs)
        step(1)
        val qin = 
          if (qs == 1) 4 - qm
          else qm
        val qmin =
          if (qs == 1 || qm == 0) 3 - qm
          else qm - 1
        val qload =
          if (qs == 1) 1
          else 0
        val qmload =
          if (qs == 0 && qm > 0) 0
          else 1
        println("Test with " + qs + " " + qm)
        expect(c.io.qin, qin)
        expect(c.io.qmin, qmin)
        expect(c.io.qload, qload)
        expect(c.io.qmload, qmload)
      }
    }
  }
}

class Radix4OnlineConverterControlTester extends ChiselFlatSpec {
  behavior of "Radix4OnlineConverterControl"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers $backend" in {
      Driver(() => new Radix4OnlineConverterControl(), backend)(c => new Radix4OnlineConverterControlTests(c)) should be (true)
    }
  }
}

