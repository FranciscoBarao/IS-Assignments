
package ejb.serverbeans;

import java.util.List;

import javax.ejb.Local;

import data.Player;

@Local
public interface PlayersEJBLocal {
    public void populate();
    public List<Player> playersTallerThan(float threshold);
}
