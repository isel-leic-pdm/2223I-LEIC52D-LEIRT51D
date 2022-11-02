package palbp.laboratory.demos.quoteofday.quotes

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import palbp.laboratory.demos.quoteofday.DependenciesContainer
import palbp.laboratory.demos.quoteofday.TAG

/**
 * We use the work manager to keep the local cache warm.
 *
 * In this case we are using a single worker to fetch both the quote of day and
 * the weeks' quotes, thereby making sure network accesses are as close to each
 * other as possible.
 *
 * We could instead chain work items (a.k.a chained tasks), if their execution
 * time was expected to be larger than the maximum allowed (currently 15 minutes)
 */
class QuotesWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params)
{
    override suspend fun doWork(): Result {
        Log.v(TAG, "QuotesWorker is executing")
        val service = (applicationContext as DependenciesContainer).quoteService
        return try {
            service.fetchQuote(mode = QuoteService.Mode.FORCE_REMOTE)
            service.fetchWeekQuotes(mode = QuoteService.Mode.FORCE_REMOTE)
            Result.success()
        }
        catch (e: Exception) {
            Result.failure()
        }
    }
}