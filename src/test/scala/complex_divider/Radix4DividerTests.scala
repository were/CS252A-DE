// See LICENSE.txt for license details.
package examples

import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

class DividerRadix4Tests(c: DividerRadix4) extends PeekPokeTester(c) {
  // 1100 0101
  val divisor = 0xC5

  poke(c.io.dividend, 0xAF)
  poke(c.io.divisor, 0xC5)
  poke(c.io.load, 1)
  step(1)
  poke(c.io.load, 0)

  for (t <- Array(
    (0xAF, 0x00, 1, 0x650, 0x158, 10),
    (0x650, 0x158, 4, 0x420, 0x280, -6),
    (0x420, 0x280, 14, 0x4A8, 0x400, -22)
  )) {
    poke(c.io.divisor, divisor)
    step(1)
    val ws_r = t._1
    val wc_r = t._2

    val y = t._6
    // expect(c.io.debug_y, y)

    val quotient = t._3
    expect(c.io.quotient, quotient)
    val remainder = t._4 & 0xFF
    expect(c.io.remainder, remainder)
    val owc_r = t._5
  }
}

class DividerRadix4Tester extends ChiselFlatSpec {
  behavior of "DividerRadix4"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers $backend" in {
      Driver(() => new DividerRadix4(8, 1), backend)(c => new DividerRadix4Tests(c)) should be (true)
    }
  }
}

