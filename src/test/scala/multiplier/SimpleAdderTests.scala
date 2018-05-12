// See LICENSE.txt for license details., first.io.out)
package multiplier

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class SimpleAdderTests(adder: SimpleAdder, width: Int) extends PeekPokeTester(adder) {
    for (i <- 0 until 100) {
        val a = rnd.nextInt(1 << (width - 1))
        val b = rnd.nextInt(1 << (width - 1))
        poke(adder.io.a, a)
        poke(adder.io.b, b)
        step(1)
        expect(adder.io.out, (a + b) & ((1 << width) - 1))
        expect(adder.io.cout, ((a + b) >> width) & 1)
    }
}

class SimpleAdderTester extends ChiselFlatSpec {
    behavior of "Adder"
    backends foreach {backend =>
        it should s"correctly add randomly generated numbers in $backend" in {
            Driver(() => new SimpleAdder(4), backend)(c => new SimpleAdderTests(c, 4)) should be (true)
        }
    }
}
