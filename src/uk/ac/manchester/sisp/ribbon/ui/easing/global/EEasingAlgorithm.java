package uk.ac.manchester.sisp.ribbon.ui.easing.global;

import uk.ac.manchester.sisp.ribbon.ui.easing.IEasingConfiguration;
import uk.ac.manchester.sisp.ribbon.utils.MathUtils;

public enum EEasingAlgorithm {
	NONE {
		@Override protected final float onCalculateEasing(final float pT, final float pB, final float pC, final float pD) { return pB + pC;                        }
	},
	LINEAR {
		@Override protected final float onCalculateEasing(final float pT, final float pB, final float pC, final float pD) { return pC * pT / pD + pB;              }
	},
	QUADRATIC_EASE_IN {
		@Override protected final float onCalculateEasing(float pT, final float pB, final float pC, final float pD)       { return pC * (pT /= pD) * (pT) + pB;    }
	},
	QUADRATIC_EASE_OUT {
		@Override protected final float onCalculateEasing(float pT, final float pB, final float pC, final float pD)       { return -pC * (pT /= pD) * (pT-2) + pB; }
	},
	QUADRATIC_EASE_IN_OUT {
		@Override protected final float onCalculateEasing(float pT, final float pB, final float pC, final float pD)       { if((pT /= (pD * 0.5f)) < 1.0f) { return pC * 0.5f * pT * pT + pB; } else { return -pC * 0.5f * ((--pT) * (pT - 2.0f) - 1.0f) + pB; } }
	},
	CUBIC_EASE_IN { 
		@Override protected final float onCalculateEasing(float pT, final float pB, final float pC, final float pD)       { return pC * (pT /= pD) * pT * pT + pB; }
	},
	CUBIC_EASE_OUT { 
		@Override protected final float onCalculateEasing(float pT, final float pB, final float pC, final float pD)       { return pC*((pT = pT/pD - 1.0f) * pT * pT + 1.0f) + pB; }
	},
	CUBIC_EASE_IN_OUT { 
		@Override protected final float onCalculateEasing(float pT, final float pB, final float pC, final float pD)       { if((pT /= pD*0.5f) < 1.0f){ return pC*0.5f*pT*pT*pT + pB; } else { return pC/2*((pT-=2)*pT*pT + 2) + pB; } }
	},
	QUARTIC_EASE_IN { 
		@Override protected final float onCalculateEasing(float pT, final float pB, final float pC, final float pD)       { return pC * (pT/=pD) * pT * pT * pT + pB; }
	},
	QUARTIC_EASE_OUT {
		@Override protected final float onCalculateEasing(float pT, final float pB, final float pC, final float pD)       { return -pC * ((pT = pT / pD - 1.0f) * pT * pT * pT - 1.0f) + pB; }
	},
	QUARTIC_EASE_IN_OUT { 
		@Override protected final float onCalculateEasing(float pT, final float pB, final float pC, final float pD)       { if((pT/= pD * 0.5f) < 1.0f) { return pC * 0.5f * pT * pT * pT * pT + pB; } else { return -pC * 0.5f * ((pT -= 2.0f) * pT * pT * pT - 2.0f) + pB; } }
	},
	QUINTIC_EASE_IN { 
		@Override protected final float onCalculateEasing(float pT, final float pB, final float pC, final float pD)       { return pC * (pT /= pD) * pT * pT * pT * pT  + pB; }
	},
	QUINTIC_EASE_OUT {
		@Override protected final float onCalculateEasing(float pT, final float pB, final float pC, final float pD)       { return pC * ((pT = pT/pD - 1.0f) * pT * pT * pT * pT + 1.0f) + pB; }
	},
	QUINTIC_EASE_IN_OUT { 
		@Override protected final float onCalculateEasing(float pT, final float pB, final float pC, final float pD)       { if((pT/=pD * 0.5f) < 1.0f) { return pC * 0.5f * pT * pT * pT * pT * pT + pB; } else { return pC * 0.5f * ((pT-=2.0f)* pT * pT * pT * pT + 2.0f) + pB; } }
	},
	SINE_EASE_IN { 
		@Override protected final float onCalculateEasing(final float pT, final float pB, final float pC, final float pD) { return -pC * (float)Math.cos(pT / pD * MathUtils.PI_OVER_2) + pC + pB; }
	},
	SINE_EASE_OUT { 
		@Override protected final float onCalculateEasing(final float pT, final float pB, final float pC, final float pD) { return pC * (float)Math.sin(pT / pD * MathUtils.PI_OVER_2) + pB; }
	},
	SINE_EASE_IN_OUT { 
		@Override protected final float onCalculateEasing(final float pT, final float pB, final float pC, final float pD) { return -pC * 0.5f * ((float)Math.cos(Math.PI * pT * pD) - 1.0f) + pB; }
	},
	EXPONENTIAL_EASE_IN { 
		@Override protected final float onCalculateEasing(final float pT, final float pB, final float pC, final float pD) { return (pT == 0.0f) ? pB : pC * (float)Math.pow(2.0, 10.0 * (pT / pD - 1.0)) + pB; }
	},
	EXPONENTIAL_EASE_OUT { 
		@Override protected final float onCalculateEasing(final float pT, final float pB, final float pC, final float pD) { return (pT == pD) ? pB + pC : pC * (-(float)Math.pow(2.0, -10.0 * pT / pD) + 1.0f) + pB;}
	},
	EXPONENTIAL_EASE_IN_OUT { 
		@Override protected final float onCalculateEasing(float pT, final float pB, final float pC, final float pD)       { if(pT == 0.0f) { return pB; } else if(pT == pD) { return pB + pC; } else if((pT /= pD * 0.5f) < 1.0f) { return pC * 0.5f * (float)Math.pow(2.0, 10.0 * (pT - 1.0)) + pB;  } else { return pC * 0.5f *(-(float)Math.pow(2.0, -10.0 * --pT) + 2.0f) + pB; } }
	},
	BOUNCE_EASE_OUT { 
		@Override protected final float onCalculateEasing(float pT, final float pB, final float pC, final float pD)       { if((pT /= pD) < (1.0f / 2.75f)) { return pC * (7.5625f * pT * pT) + pB; } else if(pT < (2.0f / 2.75f)) { return pC * (7.5625f * (pT -= (1.5f / 2.75f))* pT + 0.75f) + pB; } else if(pT < (2.5f / 2.75f)) { return pC * (7.5625f * (pT -= (2.25f/2.75f)) * pT + 0.9375f) + pB; } else { return pC * (7.5625f * (pT -= (2.625f / 2.75f)) * pT + 0.984375f) + pB; } }
	},
	BOUNCE_EASE_IN { 
		@Override protected final float onCalculateEasing(final float pT, final float pB, final float pC, final float pD) { return pC - EEasingAlgorithm.BOUNCE_EASE_OUT.onCalculateEasing(pD - pT, 0.0f, pC, pD) + pB; }
	},
	BOUNCE_EASE_IN_OUT { 
		@Override protected final float onCalculateEasing(final float pT, final float pB, final float pC, final float pD) { if(pT < pD * 0.5f) { return EEasingAlgorithm.BOUNCE_EASE_IN.onCalculateEasing(pT * 2f, 0.0f, pC, pD) * 0.5f + pB; } else { return EEasingAlgorithm.BOUNCE_EASE_OUT.onCalculateEasing(pT * 2f - pD, 0.0f, pC, pD) * 0.5f + pC * 0.5f + pB; } }
	};
	
	protected abstract float onCalculateEasing(final float pT, final float pB, final float pC, final float pD);
	
	/* Static Declarations. */
	public static final float onCalculateBoundedEasing(final IEasingConfiguration pEasingConfiguration, final float pStartTimeSeconds, final float pCurrentTimeSeconds, final float pInitialValue, final float pTerminalValue) {
		/* Calculate the CompletionTime. */
		final float lCompletionTime = pStartTimeSeconds + pEasingConfiguration.getDurationSeconds();
		/* Return the bounded EasingTime. */
		return (pCurrentTimeSeconds >= lCompletionTime) ? pTerminalValue : pEasingConfiguration.getEasingAlgorithm().onCalculateEasing(pCurrentTimeSeconds - pStartTimeSeconds, pInitialValue, pTerminalValue - pInitialValue, pEasingConfiguration.getDurationSeconds());
	}
	
}