package com.example.afreecar.model.checklist;

import androidx.annotation.NonNull;

import com.example.afreecar.model.abstraction.Equatable;
import com.example.afreecar.model.abstraction.PerfectHashable;

import java.util.List;

/**
 * The Checklist interface defines a collection of "step" elements that must fulfilled by some means.  These elements and the status of their fulfillment
 * will be displayed in a human-readable format using a wrapper object that contains the source element and their status.  The elements may be fulfilled using
 * the element itself, along with an object used to validate that its requirements have been met.
 * @param <TImpl> The class implementing this interface.
 * @param <TElement> The type of object that this checklist will contain, ie. the elements or "steps" to to be fulfilled/completed.
// * @param <TDisplay> The type of object that will be used to display an element and the status of its fulfillment.
 * @param <TFulfill> The type of object that may be validated to fulfill an element.
 */
public interface Checklist<
            TImpl extends Checklist<TImpl, TElement, TDisplay,TFulfill>,
            TElement extends Checklist.Element<TElement, TDisplay>,
            TDisplay extends Checklist.Element.Display<TDisplay, TElement>,
            TFulfill extends Equatable<TFulfill>>
        extends Equatable<TImpl>
{
    /**
     * Retrieves this checklist's elements.
     * @return a List of all elements contained in this checklist.
     */
    List<TElement> getElements();

    /**
     * Generates a display object for the input element.
     * @param checklistElement The element to display.
     * @return A display object containing the input element and the status of its fulfillment
     */
    TDisplay getDisplay(@NonNull TElement checklistElement);

    /**
     * Generates a display object for every element in this checklist.
     * @return a list containing a display object corresponding to every element in this checklist.
     */
    List<TDisplay> getDisplays();

    /**
     * Evaluates whether or not the input fulfillment is valid for the input element
     * @param element The element to be fulfilled.
     * @param fulfillObject The object whose ability to fulfill the element's requirements will be evaluated.
     * @return Whether or not the fulfillment object will be able to fulfill the element's requirements.
     */
    Boolean doesFulfill(TElement element, TFulfill fulfillObject);

    /**
     * Attempts to fulfill the input element's requirements using the input fulfillment object.
     * @param element The element to be fulfilled.
     * @param fulfillObject The object that will be used to fulfill the element's requirements, if valid.
     * @return The new status of the element's fulfill.
     */
    Boolean tryFulfill(TElement element, TFulfill fulfillObject);

    /**
     * Evaluates whether or not the input element's requirements have been fulfilled.
     * @param element The element whose fulfillment status will be evaluated.
     * @return The fulfillment status of the element.
     */
    Boolean isFulfilled(TElement element);

    /**
     * Evaluates whether or not every element of the checklist has had its requirements fulfilled.
     * @return The fulfillment status of the checklist.
     */
    Boolean isChecklistFulfilled();

    /**
     * An interface defining an element of a checklist.
     * @param <TElement> The class implementing this interface.
     */
    interface Element<TElement extends Element<TElement, TDisplay>, TDisplay extends Element.Display<TDisplay, TElement>> extends PerfectHashable<TElement> {

        @Override
        String toString();

        /**
         * Generates a display object for this element.
         * @return A display object containing this element and the status of its fulfillment.
         */
        TDisplay toDisplay(Boolean isFulfilled);

        /**
         * An interface defining a human-readable representation of the status of an element.
         * @param <TDisplay> The class implementing this interface.
         * @param <TElement> The type of element whose status will be displayed.
         */
        interface Display<TDisplay extends Display<TDisplay, TElement>, TElement extends Element<TElement, TDisplay>> extends PerfectHashable<TDisplay> {

            TElement getSource();

            Boolean isFulfilled();

            @Override
            String toString();
        }
    }
}
