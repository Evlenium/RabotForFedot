package ru.practicum.android.diploma.root.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import java.util.Locale

class RootActivity : AppCompatActivity() {
    private var _binding: ActivityRootBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigation = binding.bottomNavigation

        bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.filterCountryFragment,
                R.id.filterIndustryFragment,
                R.id.filterSettingsFragment,
                R.id.filterWorkplaceFragment,
                R.id.filterRegionFragment,
                R.id.vacancyDetailsFragment,
                ->
                    bottomNavigation.isVisible = false

                else ->
                    bottomNavigation.isVisible = true

            }
        }
        bottomNavigation.setupWithNavController(navController)
        BuildConfig.HH_ACCESS_TOKEN //for check
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateLocale(base))
        applyOverrideConfiguration(base.resources.configuration)
    }

    private fun updateLocale(context: Context): Context? {
        val ruLocale = Locale("ru")
        Locale.setDefault(ruLocale)
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(ruLocale)
        configuration.setLayoutDirection(ruLocale)
        return context.createConfigurationContext(configuration)
    }
}
