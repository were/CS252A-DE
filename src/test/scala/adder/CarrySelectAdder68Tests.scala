package adder

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class CarrySelectAdder68Tests(adder: CarrySelectAdder68) extends PeekPokeTester(adder) {
  for (i <- 0 until 100) {
    val a = Math.abs(rnd.nextInt(1 << 30))
    val b = Math.abs(rnd.nextInt(1 << 30))
    poke(adder.io.a, a)
    poke(adder.io.b, b)
    step(1)
    expect(adder.io.out, a + b)
  }
}

class CarrySelectAdder68Tester extends ChiselFlatSpec {
  behavior of "Adder"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers in $backend" in {
      Driver(() => new CarrySelectAdder68(), backend)(c => new CarrySelectAdder68Tests(c)) should be (true)
    }
  }
}
