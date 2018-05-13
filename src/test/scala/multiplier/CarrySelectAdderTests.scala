package multiplier

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class CarrySelectAdderTests(adder: CarrySelectAdder) extends PeekPokeTester(adder) {
  for (i <- 0 until 10) {
    val a = rnd.nextInt(1 << 30)
    val b = rnd.nextInt(1 << 30)
    //printf("%d\n", a)
    //printf("%d\n", b)
    poke(adder.io.a, a)
    poke(adder.io.b, b)
    step(1)
    expect(adder.io.out, (a + b) & (-1))
  }
}

class CarrySelectAdderTester extends ChiselFlatSpec {
  behavior of "Adder"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers in $backend" in {
      Driver(() => new CarrySelectAdder(List(8, 8, 8, 4, 4)), backend)(c => new CarrySelectAdderTests(c)) should be (true)
    }
  }
}
