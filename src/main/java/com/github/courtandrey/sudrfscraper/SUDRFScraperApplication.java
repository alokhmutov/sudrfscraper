package com.github.courtandrey.sudrfscraper;

import com.github.courtandrey.sudrfscraper.configuration.ApplicationConfiguration;
import com.github.courtandrey.sudrfscraper.service.SeleniumHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.time.Duration;


@SpringBootApplication
public class SUDRFScraperApplication {
	public static void main(String[] args) {
		if (args.length == 1) ApplicationConfiguration.setUsrDir(args[0]);
		ApplicationConfiguration.getInstance();
		ApplicationConfiguration.getInstance().setProperty("basic.result.path", ApplicationConfiguration.getUsrDir() + "/results/");
		SpringApplication application = new SpringApplication(SUDRFScraperApplication.class);
		application.setLazyInitialization(true);
		application.run(args);
		openStartPage();
	}

	public static void openStartPage() {
		String os = System.getProperty("os.name");
		String nul = "nul";
		if (os.toLowerCase().contains("linux")) {
			nul = "/dev/null";
			System.setProperty("webdriver.gecko.driver", ApplicationConfiguration.getUsrDir() + "/src/main/resources/linux/geckodriver");
		}
		else if (os.toLowerCase().contains("windows")) {
			System.setProperty("webdriver.gecko.driver", ApplicationConfiguration.getUsrDir() + "/src/main/resources/windows/geckodriver.exe");
		} else if (os.toLowerCase().contains("mac")) {
			nul = "/dev/null";
			System.setProperty("webdriver.gecko.driver", ApplicationConfiguration.getUsrDir() + "/src/main/resources/macOS/geckodriver");
		}

		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, nul);
		FirefoxOptions options = new FirefoxOptions();
		WebDriver wd = new FirefoxDriver(options);
		wd.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(1));
		wd.manage().timeouts().scriptTimeout(Duration.ofMinutes(1));
		wd.get("localhost:8080/");
		SeleniumHelper.setAppHolder(wd);
	}

}
