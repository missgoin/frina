        setContentView(R.layout.activity_main)
        
        findViewById<Button>(R.id.start).setOnClickListener {
            Intent(applicationContext, HEREBackgroundPositioningService::class.java).apply {
                action = LocationService.ACTION_START
                startService(this)
            }
        }
        findViewById<Button>(R.id.stop).setOnClickListener {
            Intent(applicationContext, HEREBackgroundPositioningService::class.java).apply {
                action = LocationService.ACTION_STOP
                startService(this)
            }
        }