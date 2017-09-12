package com.baliset.util.Alerts;

public enum Remedy
{
  EscalateToDevTeam,      // action may be required, but it must be escalated to developers
  NotifyDevTeam,          // development team needs to know about the issue, though no action must be taken
  Restart,                // system should be restarted to resolve immediate issues
  VerifyConfig,    // since system appears misconfigured, it requires scrutiny, fix and probably restart
  TreatAsSuspicious       // suspicious activity requires closer monitoring
}
