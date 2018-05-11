// See LICENSE.txt for license details.
package examples

import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

class CarrySaveAdderTests(c: CarrySaveAdder) extends PeekPokeTester(c) {
  for (t <- 0 until 1) {
    val rnd0 = 0x9E
    val rnd1 = 0x1
    val rnd2 = 0x33A

    poke(c.io.A, rnd0)
    poke(c.io.B, rnd1)
    poke(c.io.Cin, rnd2)
    step(1)

    expect(c.io.Sum, 0x3A5)
    expect(c.io.Cout, 0x34)
  }
}

class CarrySaveAdderTester extends ChiselFlatSpec {
  behavior of "Adder"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers $backend" in {
      Driver(() => new CarrySaveAdder(10))(c => new CarrySaveAdderTests(c)) should be (true)
    }
  }
}
