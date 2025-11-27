"use client"

import { useState } from "react"
import { ChevronDown, ChevronUp, Check } from "lucide-react"
import { Button } from "@/components/ui/button"

const CLASSIFICATION_OPTIONS = [
  { id: "positive", label: "Positive", color: "hover:bg-green-50 border-green-200 text-green-700" },
  { id: "neutral", label: "Neutral", color: "hover:bg-blue-50 border-blue-200 text-blue-700" },
  { id: "negative", label: "Negative", color: "hover:bg-red-50 border-red-200 text-red-700" },
]

const TOTAL_TASKS = 100
const SAMPLE_CONTENT =
  "This is a sample text content for labeling. You can replace this with actual image or text data from your dataset."

export default function LabelingTask() {
  const [currentTask, setCurrentTask] = useState(5)
  const [selectedClassification, setSelectedClassification] = useState<string | null>(null)
  const [instructionsOpen, setInstructionsOpen] = useState(false)
  const [submitted, setSubmitted] = useState(false)

  const handleSubmit = () => {
    if (selectedClassification) {
      setSubmitted(true)
      setTimeout(() => {
        setCurrentTask((prev) => (prev < TOTAL_TASKS ? prev + 1 : prev))
        setSelectedClassification(null)
        setSubmitted(false)
      }, 800)
    }
  }

  const progress = Math.round((currentTask / TOTAL_TASKS) * 100)

  return (
    <div className="min-h-screen bg-white flex flex-col">
      {/* Header with Progress */}
      <div className="border-b border-border px-8 py-6">
        <div className="max-w-4xl mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-semibold text-foreground">Data Labeling Task</h1>
          <div className="flex items-center gap-3">
            <div className="flex flex-col items-end gap-1">
              <span className="text-sm font-medium text-foreground">
                Task <span className="font-semibold">{currentTask}</span>/{TOTAL_TASKS}
              </span>
              <div className="w-32 h-2 bg-border rounded-full overflow-hidden">
                <div className="h-full bg-primary transition-all duration-300" style={{ width: `${progress}%` }} />
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 flex flex-col px-8 py-8">
        <div className="max-w-4xl mx-auto w-full flex flex-col gap-8">
          {/* Instructions Section */}
          <div className="border border-border rounded-lg bg-muted/30">
            <button
              onClick={() => setInstructionsOpen(!instructionsOpen)}
              className="w-full px-6 py-4 flex items-center justify-between hover:bg-muted/50 transition-colors"
            >
              <span className="font-medium text-foreground">Instructions</span>
              {instructionsOpen ? (
                <ChevronUp size={20} className="text-muted-foreground" />
              ) : (
                <ChevronDown size={20} className="text-muted-foreground" />
              )}
            </button>
            {instructionsOpen && (
              <div className="px-6 pb-4 pt-2 border-t border-border text-sm text-muted-foreground leading-relaxed">
                <p>
                  Classify the content below into one of three categories: Positive, Neutral, or Negative. Consider the
                  overall sentiment and context. Select the most appropriate label and click Submit to proceed to the
                  next task.
                </p>
              </div>
            )}
          </div>

          {/* Content Area */}
          <div className="flex flex-col gap-6 flex-1">
            <div className="bg-muted/20 border-2 border-dashed border-border rounded-lg p-12 flex items-center justify-center min-h-64">
              <div className="text-center">
                <div className="w-16 h-16 bg-border rounded-lg mx-auto mb-4 flex items-center justify-center">
                  <span className="text-3xl text-muted-foreground">📄</span>
                </div>
                <p className="text-muted-foreground leading-relaxed max-w-md">{SAMPLE_CONTENT}</p>
              </div>
            </div>

            {/* Classification Buttons */}
            <div className="flex gap-3 justify-center flex-wrap">
              {CLASSIFICATION_OPTIONS.map((option) => (
                <button
                  key={option.id}
                  onClick={() => setSelectedClassification(option.id)}
                  className={`px-6 py-3 border-2 rounded-lg font-medium transition-all ${
                    selectedClassification === option.id
                      ? `${option.color} border-current bg-current bg-opacity-10`
                      : `border-border text-foreground hover:${option.color}`
                  }`}
                >
                  <span className="flex items-center gap-2">
                    {selectedClassification === option.id && <Check size={18} />}
                    {option.label}
                  </span>
                </button>
              ))}
            </div>
          </div>

          {/* Submit Button */}
          <div className="flex justify-end pt-4">
            <Button onClick={handleSubmit} disabled={!selectedClassification} className="px-8 py-2 h-auto text-base">
              {submitted ? (
                <span className="flex items-center gap-2">
                  <Check size={18} />
                  Submitted
                </span>
              ) : (
                "Submit"
              )}
            </Button>
          </div>
        </div>
      </div>
    </div>
  )
}
