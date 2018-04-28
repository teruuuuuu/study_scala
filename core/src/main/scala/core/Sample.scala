package core

import subproject.Sample

object Core {

  def show: Unit = {
    Sample.showCount
  }
  def main(args: Array[String]) = {
    show
  }
}
