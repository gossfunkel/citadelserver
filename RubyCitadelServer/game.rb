class Game
  
  require 'timer'
  
  def initialize
    puts "Game starting..."
    $timer = Timer.new
    private $settlements = Array.new
    private $consettlements = Array.new
    private $players = Array.new
    private $settx = Array.new
    private $setty = Array.new
    
    setLevel(SpawnLevel.new("/garden.png"))
  end
  
  def start
    private $running = true
    private ticker = 0
    private month = 0
    private year = 0
    run()
  end
  
  def run
    while($running) 
      $timer.tick
      if Time.now - timer.getSecond > 1000 # currentTimeMillis
        timer.accumulateSecond
        timer.resetTick
        ticker += 1
      end
      while timer.getDelta >= 1
        update
        timer.superTick
      end
      if ticker > 30
        ticker = 0
        timer.hourTick
      end
    end
  end
  
  def stop
    $running = false
    puts "Game stopping."
  end
  
  def update
    #TODO update UI
    
    if timer.getDay
      if timer.getHour != hour
        hour = timer.getHour
      end
    end
  end
  
end