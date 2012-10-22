package sweforce.vaadin.data

import java.util.UUID

/**
 * Created with IntelliJ IDEA.
 * User: sveffa
 * Date: 10/9/12
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
trait ItemIdGenerator {

  def generateId() : AnyRef = UUID.randomUUID()

}

object DefaultIdGenerator extends ItemIdGenerator {

}


