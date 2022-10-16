package example.experminets

// 1st method:
// using functions to define behavior of each state type to event
//
// 2nd method:
// using trait to define behaviour of events

object TraitLogicModelling extends App {
  sealed trait Event
  sealed trait MemoryEvent
  sealed trait StateEvent
  sealed trait ClearingMemoryEvent
  sealed trait NotChangingStateEvent extends StateEvent
  case class IncrementCounter(n: Int) extends Event with StateEvent with MemoryEvent
  case class MemorableEvent(x: Double) extends Event with MemoryEvent with NotChangingStateEvent
  case class ShowSmile(s: String) extends Event with NotChangingStateEvent
  case class ClearState() extends Event with ClearingMemoryEvent
  // conclusion: its more clear to define login via functions then building "logic" via trait domain

  case class State(counter: Int)

  case class Memory(events: Seq[Event]) {
    def add(e: Event) = copy(events = events.appended(e))
  }

  // 1st
  def reduce(state: State, e: Event): State = e match {
    case IncrementCounter(n) => state.copy(counter = state.counter + n)
    case ShowSmile(s)        => state
    case MemorableEvent(_)   => state
    case ClearState()        => State(0)
  }

  def saveToMemory(memory: Memory, e: Event): Memory = e match {
    case e: IncrementCounter => memory.add(e)
    case ShowSmile(s)        => memory
    case e: MemorableEvent   => memory.add(e)
    case e: ClearState       => Memory(Seq())
  }

  // 2nd
  def reduce2(state: State, e: StateEvent): State = e match {
    case _: NotChangingStateEvent => state
    case IncrementCounter(n)      => state.copy(counter = state.counter + n)
  }

  def saveToMemory2(memory: Memory, e: MemoryEvent): Memory = e match {
    case IncrementCounter(n) => ???
  }
}
