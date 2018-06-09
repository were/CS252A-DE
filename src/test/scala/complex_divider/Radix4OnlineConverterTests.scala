// See LICENSE.txt for license details.
package examples

import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

class Radix4OnlineConverterTests(c: Radix4OnlineConverter) extends PeekPokeTester(c) {
  var Q = 0
  var QM = 0
  // Test with 1, 0, -2
  for (q <- Array(1, 0, -2)) {
    val qs =
      if (q < 0) 1
      else 0
    val qm =
      if (q < 0) -q
      else q
    poke(c.io.Q, Q & 0xFF)
    poke(c.io.QM, QM & 0xFF)
    poke(c.io.qm, qm)
    poke(c.io.qs, qs)
    step(1)
    Q = (Q << 2) + q
    QM = Q - 1
    expect(c.io.QOut, Q)
    expect(c.io.QMOut, QM)
  }
}

class Radix4OnlineConverterTester extends ChiselFlatSpec {
  behavior of "Radix4OnlineConverter"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers $backend" in {
      Driver(() => new Radix4OnlineConverter(8), backend)(c => new Radix4OnlineConverterTests(c)) should be (true)
    }
  }
}

