package adder

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class CarrySelectAdder16Tests(adder: CarrySelectAdder16) extends PeekPokeTester(adder) {
  for (i <- 0 until 100) {
    val a = rnd.nextInt(1 << 16)
    val b = rnd.nextInt(1 << 16)
    //printf("%d\n", a)
    //printf("%d\n", b)
    poke(adder.io.a, a)
    poke(adder.io.b, b)
    step(1)
    expect(adder.io.out, (a + b) & ((1 << 16) - 1))
  }
}

class CarrySelectAdder16Tester extends ChiselFlatSpec {
  behavior of "Adder"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers in $backend" in {
      Driver(() => new CarrySelectAdder16(), backend)(c => new CarrySelectAdder16Tests(c)) should be (true)
    }
  }
}
