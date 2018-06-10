
package examples

import chisel3._
import chisel3.util._

class Radix4Sel extends Module {
    val io = IO(new Bundle {
        val y = Input(SInt(7.W))
        val d = Input(UInt(3.W))
        val qs = Output(UInt(1.W))
        val qm = Output(UInt(2.W))
    })
    /**
     * d    0   1   2   3   4   5   6   7
     * m2   12  14  15  16  18  20  20  24
     * m1   4   4   4   4   6   6   8   8
     * m0   -4  -6  -6  -6  -8  -8  -8  -8
     * m-1  -13 -15 -16 -18 -20 -20 -22 -24
     * We select q = k if m_k <= y <= m_{k+1}
     */
    when (io.d === 0.U) {
        when (io.y < -13.S(7.W)) {
            io.qs := 1.U
            io.qm := 2.U
        } .elsewhen (io.y < -4.S(7.W)) {
            io.qs := 1.U
            io.qm := 1.U
        } .elsewhen (io.y < 4.S(7.W)) {
            io.qs := 0.U
            io.qm := 0.U
        } .elsewhen (io.y < 12.S(7.W)) {
            io.qs := 0.U
            io.qm := 1.U
        } .otherwise {
            io.qs := 0.U
            io.qm := 2.U
        }
    } .elsewhen (io.d === 1.U) {
        when (io.y < -15.S(7.W)) {
            io.qs := 1.U
            io.qm := 2.U
        } .elsewhen (io.y < -6.S(7.W)) {
            io.qs := 1.U
            io.qm := 1.U
        } .elsewhen (io.y < 4.S(7.W)) {
            io.qs := 0.U
            io.qm := 0.U
        } .elsewhen (io.y < 14.S(7.W)) {
            io.qs := 0.U
            io.qm := 1.U
        } .otherwise {
            io.qs := 0.U
            io.qm := 2.U
        }
    } .elsewhen (io.d === 2.U) {
        when (io.y < -16.S(7.W)) {
            io.qs := 1.U
            io.qm := 2.U
        } .elsewhen (io.y < -6.S(7.W)) {
            io.qs := 1.U
            io.qm := 1.U
        } .elsewhen (io.y < 4.S(7.W)) {
            io.qs := 0.U
            io.qm := 0.U
        } .elsewhen (io.y < 15.S(7.W)) {
            io.qs := 0.U
            io.qm := 1.U
        } .otherwise {
            io.qs := 0.U
            io.qm := 2.U
        }
    } .elsewhen (io.d === 3.U) {
        when (io.y < -18.S(7.W)) {
            io.qs := 1.U
            io.qm := 2.U
        } .elsewhen (io.y < -6.S(7.W)) {
            io.qs := 1.U
            io.qm := 1.U
        } .elsewhen (io.y < 4.S(7.W)) {
            io.qs := 0.U
            io.qm := 0.U
        } .elsewhen (io.y < 16.S(7.W)) {
            io.qs := 0.U
            io.qm := 1.U
        } .otherwise {
            io.qs := 0.U
            io.qm := 2.U
        }
    } .elsewhen (io.d === 4.U) {
        when (io.y < -20.S(7.W)) {
            io.qs := 1.U
            io.qm := 2.U
        } .elsewhen (io.y < -8.S(7.W)) {
            io.qs := 1.U
            io.qm := 1.U
        } .elsewhen (io.y < 6.S(7.W)) {
            io.qs := 0.U
            io.qm := 0.U
        } .elsewhen (io.y < 18.S(7.W)) {
            io.qs := 0.U
            io.qm := 1.U
        } .otherwise {
            io.qs := 0.U
            io.qm := 2.U
        }
    } .elsewhen (io.d === 5.U) {
        when (io.y < -20.S(7.W)) {
            io.qs := 1.U
            io.qm := 2.U
        } .elsewhen (io.y < -8.S(7.W)) {
            io.qs := 1.U
            io.qm := 1.U
        } .elsewhen (io.y < 6.S(7.W)) {
            io.qs := 0.U
            io.qm := 0.U
        } .elsewhen (io.y < 20.S(7.W)) {
            io.qs := 0.U
            io.qm := 1.U
        } .otherwise {
            io.qs := 0.U
            io.qm := 2.U
        }
    } .elsewhen (io.d === 6.U) {
        when (io.y < -22.S(7.W)) {
            io.qs := 1.U
            io.qm := 2.U
        } .elsewhen (io.y < -8.S(7.W)) {
            io.qs := 1.U
            io.qm := 1.U
        } .elsewhen (io.y < 8.S(7.W)) {
            io.qs := 0.U
            io.qm := 0.U
        } .elsewhen (io.y < 20.S(7.W)) {
            io.qs := 0.U
            io.qm := 1.U
        } .otherwise {
            io.qs := 0.U
            io.qm := 2.U
        }
    } .otherwise {
        when (io.y < -24.S(7.W)) {
            io.qs := 1.U
            io.qm := 2.U
        } .elsewhen (io.y < -8.S(7.W)) {
            io.qs := 1.U
            io.qm := 1.U
        } .elsewhen (io.y < 8.S(7.W)) {
            io.qs := 0.U
            io.qm := 0.U
        } .elsewhen (io.y < 24.S(7.W)) {
            io.qs := 0.U
            io.qm := 1.U
        } .otherwise {
            io.qs := 0.U
            io.qm := 2.U
        }
    }
}

class Radix4OnlineConverterControl() extends Module {
    val io = IO(new Bundle {
        val qm = Input(UInt(2.W))
        val qs = Input(UInt(1.W))
        val qin = Output(UInt(2.W))
        val qmin = Output(UInt(2.W))
        // For multiplexer, 0 => q, 1 => qm
        val qload = Output(UInt(1.W))
        val qmload = Output(UInt(1.W))
    })

    when (io.qs === 1.U) {
        // q < 0
        // qin = 4 - qm = 0b10 | qm(0)
        io.qin := util.Cat(1.U(1.W), io.qm(0))
        // qmin = 3 - qm = ~qm
        io.qmin := ~io.qm
        io.qload := 1.U(1.W)
        io.qmload := 1.U(1.W)
    } .elsewhen (io.qm === 0.U) {
        // q = 0// qmin = 3 - qm
        io.qin := 0.U(2.W)
        io.qmin := 3.U(2.W)
        io.qload := 0.U(1.W)
        io.qmload := 1.U(1.W)
    } .otherwise {
        // q > 0
        io.qin := io.qm
        // q = 1, 2
        // qmin = qm - 1 = qm >> 1
        io.qmin := util.Cat(0.U(1.W), io.qm(1))
        io.qload := 0.U(1.W)
        io.qmload := 0.U(1.W)
    }
}

class Radix4OnlineConverter(val n: Int) extends Module {
    val io = IO(new Bundle {
        val Q = Input(UInt(n.W))
        val QM = Input(UInt(n.W))
        val qm = Input(UInt(2.W))
        val qs = Input(UInt(1.W))
        val QOut = Output(UInt(n.W))
        val QMOut = Output(UInt(n.W))
    })

    val control = Module(new Radix4OnlineConverterControl())
    control.io.qm := io.qm
    control.io.qs := io.qs

    val mux_q = Module(new Multiplexer(n))
    val mux_qm = Module(new Multiplexer(n))

    mux_q.io.a := io.Q
    mux_q.io.b := io.QM
    mux_q.io.sel := control.io.qload
    mux_qm.io.a := io.Q
    mux_qm.io.b := io.QM
    mux_qm.io.sel := control.io.qmload

    io.QOut := util.Cat(mux_q.io.o(n - 2, 0), control.io.qin)
    io.QMOut := util.Cat(mux_qm.io.o(n - 2, 0), control.io.qmin)
}

class Radix4Updater(val n: Int) extends Module {
    val int_bits = 3
    val io = IO(new Bundle {
        val ws_r = Input(UInt((n + int_bits).W))
        val wc_r = Input(UInt((n + int_bits).W))
        val divisor = Input(UInt(n.W))
        val qs = Output(UInt(1.W))
        val qm = Output(UInt(2.W))
        val ows_r = Output(UInt((n + int_bits).W))
        val owc_r = Output(UInt((n + int_bits).W))
        val debug_y = Output(SInt(7.W))
    })

    val selector = Module(new Radix4Sel())
    // y = xxx.xxxx
    val ni = n + int_bits
    selector.io.y := (io.ws_r(ni - 1, ni - 7) + io.wc_r(ni - 1, ni - 7)).asSInt
    io.debug_y := (io.ws_r(ni - 1, ni - 7) + io.wc_r(ni - 1, ni - 7)).asSInt
    // d = 0.1xxx
    selector.io.d := io.divisor(n - 2, n - 4)

    io.qs := selector.io.qs
    io.qm := selector.io.qm

    // Feed to csa.
    val csa = Module(new CarrySaveAdder(n + 3))
    csa.io.A := io.ws_r
    when (selector.io.qs === 0.U) {
        when (selector.io.qm === 0.U) {
            // q === 0.
            csa.io.B := io.wc_r
            csa.io.Cin := 0.U((n + int_bits).W)
        } .elsewhen (selector.io.qm === 1.U) {
            // q === 1
            // The least significant bit is set to 1
            // due to 2's complement of -q * divisor
            csa.io.B := util.Cat(io.wc_r(n + int_bits - 1, 1), 1.U(1.W))
            csa.io.Cin := util.Cat(1.U(int_bits.W), ~io.divisor)
        } .otherwise {
            // q === 2
            csa.io.B := util.Cat(io.wc_r(n + int_bits - 1, 1), 1.U(1.W))
            csa.io.Cin := util.Cat(1.U((int_bits - 1).W), ~io.divisor, 1.U(1.W))
        }
    } .otherwise {
        when (selector.io.qm === 1.U) {
            // q === -1
            csa.io.B := io.wc_r
            csa.io.Cin := util.Cat(0.U(int_bits.W), io.divisor)
        } .otherwise {
            // q === -2
            csa.io.B := io.wc_r
            csa.io.Cin := util.Cat(0.U((int_bits - 1).W), io.divisor, 0.U(1.W))
        }
    }

    io.ows_r := csa.io.Sum << 2
    io.owc_r := csa.io.Cout << 2
}

/**
 * The quotient digit size is [-2, 2] (a = 2).
 * The redundancy factor is rho = a / (r - 1) = 2 / 3
 */
class DividerRadix4(val n: Int, val m: Int) extends Module {
    val io = IO(new Bundle {
        val dividend = Input(UInt(n.W))
        val divisor = Input(UInt(n.W))
        val load = Input(Bool())
        val quotient = Output(UInt(n.W))
        val remainder = Output(UInt(n.W))
    })
    
    // Note this is not actually ws, wc but r * ws, r * wc.
    val int_bits = 3
    val ws_r = Reg(UInt((n + int_bits).W))
    val wc_r = Reg(UInt((n + int_bits).W))

    val updaters = Array.fill(m)(Module(new Radix4Updater(n)))
    updaters(0).io.ws_r := ws_r
    updaters(0).io.wc_r := wc_r
    updaters(0).io.divisor := io.divisor
    for (i <- 1 until m) {
        // Connect all the updaters to the previous
        updaters(i).io.ws_r := updaters(i - 1).io.ows_r
        updaters(i).io.wc_r := updaters(i - 1).io.owc_r
        updaters(i).io.divisor := io.divisor
    }
    
    val Q = Reg(UInt(n.W))
    val QM = Reg(UInt(n.W))

    io.quotient := Q

    val converters = Array.fill(m)(Module(new Radix4OnlineConverter(n)))
    converters(0).io.Q := Q
    converters(0).io.QM := QM
    for (i <- 1 until m) {
        // Connect all converters to the previous one.
        // converters(i).io.load := io.load
        converters(i).io.Q := converters(i - 1).io.QOut
        converters(i).io.QM := converters(i - 1).io.QMOut
    }

    // Connect all the converter to the corresponding updater
    for (i <- 0 until m) {
        converters(i).io.qs := updaters(i).io.qs
        converters(i).io.qm := updaters(i).io.qm
    }

    when (io.load) {
        /**
         * Initialization.
         * Since redundancy factor is 0.667, we make w[0] = dividend / 4.
         * But since we make ws_r = ws * r = ws * 4, we can initialize it to
         * dividend.
         */
        ws_r := util.Cat(0.U(int_bits.W), io.dividend)
        wc_r := 0.U
        Q := 0.U
        QM := 0.U
    } .otherwise {
        /**
         * Recurrence.
         */
        ws_r := updaters(m - 1).io.ows_r
        wc_r := updaters(m - 1).io.owc_r
        // Converter
        Q := converters(m - 1).io.QOut
        QM := converters(m - 1).io.QMOut
    }

    io.remainder := ws_r(n - 1, 0)
}