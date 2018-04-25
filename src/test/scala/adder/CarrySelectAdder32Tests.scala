package adder

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class CarrySelectAdder32Tests(adder: CarrySelectAdder32) extends PeekPokeTester(adder) {
  for (i <- 0 until 100) {
    val a = rnd.nextInt(1 << 30)
    val b = rnd.nextInt(1 << 30)
    //printf("%d\n", a)
    //printf("%d\n", b)
    poke(adder.io.a, a)
    poke(adder.io.b, b)
    step(1)
    expect(adder.io.out, a + b)
  }
}

class CarrySelectAdder32Tester extends ChiselFlatSpec {
  behavior of "Adder"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers in $backend" in {
      Driver(() => new CarrySelectAdder32(), backend)(c => new CarrySelectAdder32Tests(c)) should be (true)
    }
  }
}
