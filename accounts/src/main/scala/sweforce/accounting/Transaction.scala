package sweforce.accounting

import java.util.UUID

import sweforce.accounting.Account.AccountCmd

case class Transaction(id : UUID, entries : Seq[AccountCmd])


