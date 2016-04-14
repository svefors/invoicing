package sweforce.invoicing.accounts.domain


sealed trait VatLevel

case object Normal extends VatLevel
case object Reduced extends VatLevel
case object FurterReduced extends VatLevel

