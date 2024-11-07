/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.todolist.ui.screens.edittask

enum class EditFlagOption {
  On,
  Off;

  companion object {
    fun getByCheckedState(checkedState: Boolean?): EditFlagOption {
      val hasFlag = checkedState ?: false
      return if (hasFlag) On else Off
    }

    fun getBooleanValue(flagOption: String): Boolean {
      return flagOption == On.name
    }

    fun getOptions(): List<String> {
      val options = mutableListOf<String>()
      entries.forEach { flagOption -> options.add(flagOption.name) }
      return options
    }
  }
}