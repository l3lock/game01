package sut.game01.core.character;

import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;
import playn.core.util.Callback;

public class Zealot {

	private Sprite sprite;
	private int si = 0;
	private boolean hasLoaded = false;
	private Zealot z;

	public enum State {
		IDLE, RUN, ATK
	};

	private State state = State.IDLE;

	private int e = 0;
	private int offset = 8;

	public Zealot(final float x, final float y) {

		sprite = SpriteLoader.getSprite("images/zealot.json");
		sprite.addCallback(new Callback<Sprite>() {

			@Override
			public void onSuccess(Sprite result) {
				sprite.setSprite(si);
				sprite.layer().setOrigin(sprite.width()  / 2f,
										 sprite.height() / 2f);
				sprite.layer().setTranslation(x, y + 13f);
				hasLoaded = true;
			}

			@Override
			public void onFailure(Throwable cause) {
				PlayN.log().error("Error loading image!", cause);
			}

		});
	}

	public Layer layer() {
		return sprite.layer();
	}

	public void update(int delta) {
		if(hasLoaded == false) return;

		PlayN.keyboard().setListener(new Keyboard.Adapter() {
			@Override
			public void onKeyUp(Keyboard.Event event) {
				if(event.key() == Key.SPACE) {
					switch(state) {
						case IDLE: state = State.RUN;break;
						case RUN: state = State.ATK;break;
						case ATK: state = State.IDLE;break;
					}
				}
			}
		});

		e += delta;
		if(e > 150) {
			switch(state) {
				case IDLE: offset = 0;break;
				case RUN:  offset = 4;break;
				case ATK:  offset = 8;break;
			}

			si = offset +((si + 1) % 4);
			sprite.setSprite(si);
			e = 0;
		}
	}
}