package vexriscv.demo

import vexriscv.plugin._
import vexriscv.{plugin, VexRiscv, VexRiscvConfig}
import spinal.core._

/**
 * Created by spinalvm on 15.06.17.
 */
object GenSmallest extends App{
  def cpu() = new VexRiscv(
    config =     VexRiscvConfig(
      plugins = List(
        new IBusSimplePlugin(
          resetVector = 0x00000000,
          cmdForkOnSecondStage = true,
          cmdForkPersistence = false,
          prediction = NONE,
          catchAccessFault = false,
          compressedGen = true
        ),
        new DBusSimplePlugin(
          catchAddressMisaligned = true,
          catchAccessFault = true,
          earlyInjection = true
        ),
        new CsrPlugin(CsrPluginConfig(
          catchIllegalAccess = false,
          mvendorid      = null,
          marchid        = null,
          mimpid         = null,
          mhartid        = null,
          misaExtensionsInit = 66,
          misaAccess     = CsrAccess.NONE,
          mtvecAccess    = CsrAccess.NONE,
          mtvecInit      = 4l,
          mepcAccess     = CsrAccess.NONE,
          mscratchGen    = true,
          mcauseAccess   = CsrAccess.READ_ONLY,
          mbadaddrAccess = CsrAccess.NONE,
          mcycleAccess   = CsrAccess.NONE,
          minstretAccess = CsrAccess.NONE,
          ecallGen       = true,
          wfiGenAsWait   = false,
          ucycleAccess   = CsrAccess.NONE,
          uinstretAccess = CsrAccess.NONE
        )),
        new DecoderSimplePlugin(
          catchIllegalInstruction = true
        ),
        new RegFilePlugin(
          regFileReadyKind = plugin.SYNC,
          zeroBoot = true
        ),
        new IntAluPlugin,
        new SrcPlugin(
          separatedAddSub = false,
          executeInsertion = true
        ),
        new FullBarrelShifterPlugin(earlyInjection = true),
        new HazardSimplePlugin(
          bypassExecute           = true,
          bypassMemory            = true,
          bypassWriteBack         = true,
          bypassWriteBackBuffer   = true,
          pessimisticUseSrc       = false,
          pessimisticWriteRegFile = false,
          pessimisticAddressMatch = false
        ),
        new MulDivIterativePlugin(
          genMul = true,
          genDiv = true,
          mulUnrollFactor = 2,
          divUnrollFactor = 2
        ),
        new BranchPlugin(
          earlyBranch = true,
          catchAddressMisaligned = false
        )
      )
    )
  )
  SpinalVerilog(cpu())
}