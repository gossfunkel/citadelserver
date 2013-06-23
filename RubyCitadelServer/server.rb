class Server
  
  require 'socket'
  
  def initialize
    puts "Server starting up at #{Time.now}..."
  end
  
  def start
    $running = true
    $speech = Array.new
    $connectedPlayers = Array.new
    $socket = UDPSocket.new
    $socket.bind(nil, 1042)
    run
  end
  
  def run
    while($running)
      begin
        text, host = $socket.recvfrom(1024)
        parsePacket(text, host)
      rescue
        if $running
          puts "Server receive interrupted!"
          exit 1
        else 
          #server has been closed
        end
      end
      puts text
    end
  end
  
  def stop
    $running = false
    $socket.close
    puts "Server going down at #{Time.now}."
  end
  
  def at_exit
    $running = false
    $socket.close
    puts "Socket emergency exit"
  end
  
  def parsePacket (text, address) #TODO add port
    puts text
    
  end
  
end