class Timer
  
  # -------------------- constructors --------------------------------------
  
  def initialize
    #TODO more precise time function
    $lastTime = Time.now #nanoTime
    private fps = 60.0
    private ns = 1000000000.0 / fps;
    private day = true
    
    private now = lastTime
    private delta = 0.0
    private frames = 0
    private updates = 0
    private second = Time.now #currentTimeMillis
  end
  
  # -------------------- methods -------------------------------------------
  
  def tick
    lastTime = now
    now = Time.now #nanoTime
    delta += (now - lastTime) / ns
    frames += 1
  end
  
  def superTick
    updates += 1
    delta -= 1
    lastTime = now
  end
  
  def hourTick
    hour += 1
    if hour == 12
      day = !day
      hour = 0
    end
  end
  
  def resetTick
    updates = 0
    frames = 0
  end
  
  def returnFPS
    String.new(updates + " ups, " + frames + " fps")
  end
  
  def accumulateSecond
    second += 1000
  end
  
  # -------------------- getters -------------------------------------------
  
  def getDelta
    delta
  end
  
  def getFPS
    frames
  end
  
  def getSecond
    second
  end
  
  def getHour
    hour
  end
  
  def getDay
    day
  end
  
  # -------------------- setters -------------------------------------------
  
  def setHour (h)
    hour = h
  end
  
end