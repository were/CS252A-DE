// See LICENSE.txt for license details.
package examples

import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

class Radix4UpdaterTests(c: Radix4Updater) extends PeekPokeTester(c) {
  // 1100 0101
  val divisor = 0xC5
  for (t <- Array(
    (0xAF, 0x00, 1, 0x650, 0x158, 10),
    (0x650, 0x158, 0, 0x420, 0x280, -6),
    (0x420, 0x280, -2, 0x4A8, 0x400, -22)
  )) {
    val ws_r = t._1
    val wc_r = t._2
    poke(c.io.ws_r, ws_r)
    poke(c.io.wc_r, wc_r)
    poke(c.io.divisor, divisor)
    step(1)

    val y = t._6
    expect(c.io.debug_y, y)

    val q = t._3
    val qs =
      if (q < 0) 1
      else 0
    val qm =
      if (q < 0) -q
      else q
    expect(c.io.qs, qs)
    expect(c.io.qm, qm)
    val ows_r = t._4
    val owc_r = t._5
    expect(c.io.owc_r, owc_r)
    expect(c.io.ows_r, ows_r)
  }
}

class Radix4UpdaterTester extends ChiselFlatSpec {
  behavior of "Radix4Updater"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers $backend" in {
      Driver(() => new Radix4Updater(8), backend)(c => new Radix4UpdaterTests(c)) should be (true)
    }
  }
}

