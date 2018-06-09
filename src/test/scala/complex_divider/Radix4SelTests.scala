// See LICENSE.txt for license details.
package examples

import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

class Radix4SelTests(c: Radix4Sel) extends PeekPokeTester(c) {
  for (d <- 0 until 8) {
    for (y <- -63 until 64) {
      poke(c.io.y, y)
      poke(c.io.d, d)
      step(1)
      val result = d match {
        case 0 => {
          if (y < -13)      (1, 2)
          else if (y < -4)  (1, 1)
          else if (y < 4)   (0, 0)
          else if (y < 12)  (0, 1)
          else              (0, 2)
        }
        case 1 => {
          if (y < -15)      (1, 2)
          else if (y < -6)  (1, 1)
          else if (y < 4)   (0, 0)
          else if (y < 14)  (0, 1)
          else              (0, 2)
        }
        case 2 => {
          if (y < -16)      (1, 2)
          else if (y < -6)  (1, 1)
          else if (y < 4)   (0, 0)
          else if (y < 15)  (0, 1)
          else              (0, 2)
        }
        case 3 => {
          if (y < -18)      (1, 2)
          else if (y < -6)  (1, 1)
          else if (y < 4)   (0, 0)
          else if (y < 16)  (0, 1)
          else              (0, 2)
        }
        case 4 => {
          if (y < -20)      (1, 2)
          else if (y < -8)  (1, 1)
          else if (y < 6)   (0, 0)
          else if (y < 18)  (0, 1)
          else              (0, 2)
        }
        case 5 => {
          if (y < -20)      (1, 2)
          else if (y < -8)  (1, 1)
          else if (y < 6)   (0, 0)
          else if (y < 20)  (0, 1)
          else              (0, 2)
        }
        case 6 => {
          if (y < -22)      (1, 2)
          else if (y < -8)  (1, 1)
          else if (y < 8)   (0, 0)
          else if (y < 20)  (0, 1)
          else              (0, 2)
        }
        case 7 => {
          if (y < -24)      (1, 2)
          else if (y < -8)  (1, 1)
          else if (y < 8)   (0, 0)
          else if (y < 24)  (0, 1)
          else              (0, 2)
        }
      }
      println("Test with " + d + " " + y)
      expect(c.io.qs, result._1)
      expect(c.io.qm, result._2)
    }
  }
}

class Radix4SelTester extends ChiselFlatSpec {
  behavior of "Radix4Sel"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers $backend" in {
      Driver(() => new Radix4Sel(), backend)(c => new Radix4SelTests(c)) should be (true)
    }
  }
}

