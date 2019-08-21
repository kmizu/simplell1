package com.github.kmizu;

import com.github.kmizu.simplell1.SimpleLL1Parser
import wvlet.airspec._

class SimpleLL1ParserSpec extends AirSpec {
  def `number is parsed`(): Unit = {
    val parser = new SimpleLL1Parser("1")
    parser.expression() shouldBe 1

    parser.reset("10")
    parser.expression() shouldBe 10

    parser.reset("9999")
    parser.expression() shouldBe 9999

    parser.reset("0")
    parser.expression() shouldBe 0
  }

  def `addition is parsed`(): Unit = {
    val parser = new SimpleLL1Parser("0+0")
    parser.expression() shouldBe 0

    parser.reset("0+1")
    parser.expression() shouldBe 1

    parser.reset("1+1")
    parser.expression() shouldBe 2

    parser.reset("2+1")
    parser.expression() shouldBe 3

    parser.reset("10+10")
    parser.expression() shouldBe 20
  }

  def `subtraction is parsed`(): Unit = {
    val parser = new SimpleLL1Parser("0-0")
    parser.expression() shouldBe 0

    parser.reset("0-1")
    parser.expression() shouldBe -1

    parser.reset("1-1")
    parser.expression() shouldBe 0

    parser.reset("2-1")
    parser.expression() shouldBe 1

    parser.reset("10-10")
    parser.expression() shouldBe 0
  }

  def `multiplication is parsed`(): Unit = {
    val parser = new SimpleLL1Parser("0*0")
    parser.expression() shouldBe 0

    parser.reset("0*1")
    parser.expression() shouldBe 0

    parser.reset("1*1")
    parser.expression() shouldBe 1

    parser.reset("2*1")
    parser.expression() shouldBe 2

    parser.reset("10*10")
    parser.expression() shouldBe 100
  }

  def `division is parsed`(): Unit = {
    val parser = new SimpleLL1Parser("0/0")
    intercept[ArithmeticException] {
      parser.expression()
    }

    parser.reset("0/1")
    parser.expression() shouldBe 0

    parser.reset("1/1")
    parser.expression() shouldBe 1

    parser.reset("2/1")
    parser.expression() shouldBe 2

    parser.reset("10/10")
    parser.expression() shouldBe 1

    parser.reset("100/10")
    parser.expression() shouldBe 10
  }

  def `complex expression is parsed`(): Unit = {
    val parser = new SimpleLL1Parser("1+2*3")
    parser.expression() shouldBe 7

    parser.reset(("(1+2)*3"))
    parser.expression() shouldBe 9

    parser.reset("1+2*3-1/4")
    parser.expression() shouldBe 7

    parser.reset("(1+2*3-1)/4")
    parser.expression() shouldBe 1
  }
}
