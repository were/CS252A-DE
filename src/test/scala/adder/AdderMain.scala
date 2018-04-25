// See README.md for license details.

package adder

import chisel3._

object CarrySelectAdderMain extends App {
  iotesters.Driver.execute(args, () => new CarrySelectAdder16) {
    c => new CarrySelectAdder16Tests(c)
  }
}

object GCDRepl extends App {
  iotesters.Driver.executeFirrtlRepl(args, () => new CarrySelectAdder16)
}
