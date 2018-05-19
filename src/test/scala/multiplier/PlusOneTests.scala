package multiplier

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class AddOneTests(adder: AddOne) extends PeekPokeTester(adder) {
  for (i <- 0 until 10) {
    val a = rnd.nextInt(1 << 4)
    //printf("%d\n", a)
    //printf("%d\n", b)
    poke(adder.io.a, a)
    step(1)
    expect(adder.io.out, (a + 1) & ((1 << 4) - 1))
  }
}

class AddOneTester extends ChiselFlatSpec {
  behavior of "Adder"
  backends foreach {backend =>
    it should s"correctly add randomly generated numbers in $backend" in {
      Driver(() => new AddOne(4), backend)(c => new AddOneTests(c)) should be (true)
    }
  }
}
