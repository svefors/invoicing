package sweforce.invoicing.accounts.domain

import java.util.UUID

sealed trait AccountType

case object Asset extends AccountType
case object Debt extends AccountType
case object Equity extends AccountType
case object Income extends AccountType
case object Expense extends AccountType







