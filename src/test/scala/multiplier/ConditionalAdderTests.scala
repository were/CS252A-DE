package multiplier

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class ConditionalAdderTests(adder: ConditionalAdder, width: Int) extends PeekPokeTester(adder) {
  for (i <- 0 until 100) {
    val a = rnd.nextInt(1 << width)
    val b = rnd.nextInt(1 << width)
    //printf("%d %d\n", a, b)
    poke(adder.io.a, a)
    poke(adder.io.b, b)
    step(1)
    val width_bits_ones = ((1 << width) - 1)
    expect(adder.io.sum0, (a + b)     & width_bits_ones)
    expect(adder.io.sum1, (a + b + 1) & width_bits_ones)
    expect(adder.io.cout0, ((a + b) >> width)     & 1)
    expect(adder.io.cout1, ((a + b + 1) >> width) & 1)
  }
}

class ConditionalAdderTester() extends ChiselFlatSpec {
  behavior of "Adder"
  backends foreach {backend =>
     it should s"correctly add randomly generated numbers in $backend" in {
       Driver(() => new ConditionalAdder(6), backend)(c => new ConditionalAdderTests(c, 6)) should be (true)
     }
  }
}
