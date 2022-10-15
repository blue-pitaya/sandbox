package example.museum

import slick.jdbc.PostgresProfile.api._

/*
 * Hero has equipment slots: (left hand, right hand, head, torso, left ring, right ring and legs)
 * Hero has item_instance_id for each slot, and item_instace has item_id which refs to item stats, name etc.
 * so if you want to get item name for each item in eq you kinda have to make 14 joins
 * i assume it can be done easier, but i just wanted to perserve this query because it looks fun
 */

object MyQuery {
  import slick14joins.TableQueries._
  //format: off
  val q = heroes
    .joinLeft(itemInstances)
    .on { case (h, ii) => h.eqRightHand === ii.id }
    .joinLeft(items)
    .on { case ((_, ii), i) => ii.map(_.itemId) === i.id }
    .map { case ((h, _), rhI) => (h, rhI) }
    .joinLeft(itemInstances)
    .on { case ((h, _), ii) => h.eqLeftHand === ii.id }
    .joinLeft(items)
    .on { case ((_, ii), i) => ii.map(_.itemId) === i.id }
    .map { case (((h, rhI), _), lfI) => (h, rhI, lfI) }
    .joinLeft(itemInstances)
    .on { case ((h, _, _), ii) => h.eqHead === ii.id }
    .joinLeft(items)
    .on { case ((_, ii), i) => ii.map(_.itemId) === i.id }
    .map { case (((h, rhI, lhI), _), hI) => (h, rhI, lhI, hI) }
    .joinLeft(itemInstances)
    .on { case ((h, _, _, _), ii) => h.eqTorso === ii.id }
    .joinLeft(items)
    .on { case ((_, ii), i) => ii.map(_.itemId) === i.id }
    .map { case (((h, rhI, lhI, hI), _), tI) => (h, rhI, lhI, hI, tI) }
    .joinLeft(itemInstances)
    .on { case ((h, _, _, _, _), ii) => h.eqLeftRing === ii.id }
    .joinLeft(items)
    .on { case ((_, ii), i) => ii.map(_.itemId) === i.id }
    .map { case (((h, rhI, lhI, hI, tI), _), lrI) => (h, rhI, lhI, hI, tI, lrI) }
    .joinLeft(itemInstances)
    .on { case ((h, _, _, _, _, _), ii) => h.eqRightRing === ii.id }
    .joinLeft(items)
    .on { case ((_, ii), i) => ii.map(_.itemId) === i.id }
    .map { case (((h, rhI, lhI, hI, tI, lrI), _), rrI) => (h, rhI, lhI, hI, tI, lrI, rrI) }
    .joinLeft(itemInstances)
    .on { case ((h, _, _, _, _, _, _), ii) => h.eqLegs === ii.id }
    .joinLeft(items)
    .on { case ((_, ii), i) => ii.map(_.itemId) === i.id }
    .map { case (((h, rhI, lhI, hI, tI, lrI, rrI), _), lI) => (h, rhI, lhI, hI, tI, lrI, rrI, lI) }
    .result
    .headOption
  //format: on
}
