package sweforce.vaadin.data

import com.vaadin.data.{Buffered}

/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 9/15/12
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */

trait Buffer[T] extends FProperty[T] with Buffered{

  var buffered = true;

  def isModified = bufferModified

  def isBuffered = buffered

  def setBuffered(buffered: Boolean) { throw new UnsupportedOperationException()}

  def setReadThrough(readThrough: Boolean) {throw new UnsupportedOperationException()}

  def isReadThrough = !buffered

  def setWriteThrough(writeThrough: Boolean) {throw new UnsupportedOperationException()}

  def isWriteThrough = !buffered

  /*
  override def getValue = getter
      getter: => T, setter: (T) => Unit
  override def setValue(newValue: Any) {
    setter(newValue.asInstanceOf[T])
  }
   */
//  val getter: => T
//  val setter: (T) => Unit

//  def getter:  T
//
//  def setter(value : T) : Unit

  private[data] var bufferedValue: T = _

  private[data] var bufferModified : Boolean = false

  def setBufferedValue(newValue: T) = {
    bufferModified = true
    bufferedValue = newValue
  }

  def discard() = {
    bufferModified = false
  }

  def commit() = {
    bufferModified = false
    setValue(bufferedValue)
  }

  def getBufferedValue = {
    if (bufferModified)
      bufferedValue
    else
      getValue
  }


}